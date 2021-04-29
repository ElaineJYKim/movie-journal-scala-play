package models

import collection.mutable

object journalModel {
  val journals = mutable.Map[String, List[String]]("Into the Wild" -> List("2007", "Sean Penn"))

  def validateJournal(title:String, year:String, director:String): Boolean = {
    if (journals.contains(title)) false
    else true
  }

  def createJournal(title:String, year:String, director:String): Boolean = {
    if (journals.contains(title)) false
    else {
      journals(title) = List(year, director)
      true
    }
  }

  def getJournal(title:String): Seq[String] = {
    journals.get(title).getOrElse(Nil)
  }

  def removeJournal(title:String): Unit = ???
}