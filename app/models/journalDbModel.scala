package models

import javac.bertModel.BertModel
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext
import models.Tables._

import scala.concurrent.Future

class journalDbModel(db: Database, model: BertModel)(implicit ec: ExecutionContext) {

  def createJournal(title:String, year:String, director:String): Future[Boolean] = {
    val duplicateJournal = db.run(Journal.filter(journalRow => journalRow.title === title).result)
    duplicateJournal.map { journalRow =>
      if (journalRow.isEmpty) {
        db.run(Journal += JournalRow(-1, title, year, director))
        true
      } else false
    }
  }

  def validateJournal(title:String): Future[Option[String]] = {
    val duplicateJournal = db.run(Journal.filter(journalRow => journalRow.title === title).result)
    duplicateJournal.map(journalRows => journalRows.headOption.flatMap {
      journalRow => if (journalRow.title.equals(title)) Some(journalRow.title) else None
    })
  }

  def getJournals(): Future[Seq[JournalRow]] = {
    val rows = for (row <- Journal) yield row
    val res = rows.result
    db.run(res)
  }

  def getJournal(id: Int): Future[JournalRow] = {
    val journal = db.run(Journal.filter( journalRow => journalRow.id === id ).result)
    journal.map { rowSeq =>
      rowSeq(0)
    }
  }

  def deleteJournal(id: Int): Unit = {
    deleteEntries(id)
    db.run(Journal.filter( journalRow => journalRow.id === id ).delete)
  }

  def deleteEntries(id: Int): Unit = {
    db.run(Entries.filter( entryRow => entryRow.journalId === id).delete)
  }


  // ENTRIES
  def createEntry(entry:String, journalId: Int): Future[Boolean] = Future {
    val sentiment = getSentiment(entry)
    db.run(Entries += EntriesRow(-1, entry, sentiment, Some(journalId)))
    true
  }

  def getEntries(id: Int): Future[Seq[EntriesRow]] = {
    val entries = db.run(Entries.filter( entryRow => entryRow.journalId === id).result)
    entries
  }

  def getSentiment(entry: String): Int = {
    model.predict(entry)
  }

}
