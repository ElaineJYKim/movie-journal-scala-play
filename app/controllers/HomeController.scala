package controllers

import javac.bertModel.BertModel
import javac.javaTokenization.FullTokenizer

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class MovieData(title:String, year:String, Director:String)
case class EntryData(entry:String)

@Singleton
class HomeController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  val tokenizer = new FullTokenizer()
  val bertModel = new BertModel(tokenizer)
  private val model = new journalDbModel(db, bertModel)

  val movieForm = Form (mapping(
    "Title" -> nonEmptyText,
    "Year" -> nonEmptyText,
    "Director" -> nonEmptyText,
  )(MovieData.apply)(MovieData.unapply))

  val entryForm = Form (mapping(
    "Entry" -> nonEmptyText,
  )(EntryData.apply)(EntryData.unapply))

  def index() = Action.async { implicit request =>
    model.getJournals().map {
      sequence => Ok(views.html.index(sequence))
    }
  }

  def addMovie() = Action { implicit request =>
    Ok(views.html.add(movieForm))
  }

  def addMovieForm() = Action.async { implicit request =>
    movieForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest(views.html.add(formWithErrors))),
      md =>
        model.createJournal(md.title, md.year, md.Director).map { valueCreated =>
          if (valueCreated) Redirect(routes.HomeController.index())
          else Redirect(routes.HomeController.addMovie())
        }
    )
  }

  def addEntryForm(id: Int) = Action.async { implicit request =>
    entryForm.bindFromRequest.fold(
      formWithErrors => {
        model.getJournal(id).map {
          journalRow => model.getEntries(id).map {
            entriesSeq => Future(BadRequest(views.html.journal(formWithErrors, journalRow, entriesSeq)))
          }
        }.flatten
      }.flatten,
      ed =>
        model.createEntry(ed.entry, id).map { entryCreated =>
          if (entryCreated) Redirect(routes.HomeController.journal(id))
          else Redirect(routes.HomeController.journal(id))
        }
    )
  }

  def journal(id: Int) = Action.async { implicit request =>
    model.getJournal(id).map {
      journalRow => model.getEntries(id).map {
        entriesSeq => Ok(views.html.journal(entryForm, journalRow, entriesSeq))
      }
    }.flatten
  }

  def deleteJournal(id:Int) = Action { implicit request =>
    model.deleteJournal(id)
    Redirect(routes.HomeController.index())
  }

  def deleteEntries(id: Int) = Action { implicit request =>
    model.deleteEntries(id)
    Redirect(routes.HomeController.journal(id))
  }

  def tester(title: String) = Action { implicit request =>

    val token_ids = bertModel.predict(title)
    Ok(views.html.tester(s"$token_ids"))
  }

}
