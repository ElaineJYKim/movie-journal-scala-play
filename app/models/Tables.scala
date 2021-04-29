package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Entries.schema ++ Journal.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Entries
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param entry Database column entry SqlType(varchar), Length(100,true)
   *  @param sentiment Database column sentiment SqlType(int4)
   *  @param journalId Database column journal_id SqlType(int4), Default(None) */
  case class EntriesRow(id: Int, entry: String, sentiment: Int, journalId: Option[Int] = None)
  /** GetResult implicit for fetching EntriesRow objects using plain SQL queries */
  implicit def GetResultEntriesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]]): GR[EntriesRow] = GR{
    prs => import prs._
    EntriesRow.tupled((<<[Int], <<[String], <<[Int], <<?[Int]))
  }
  /** Table description of table entries. Objects of this class serve as prototypes for rows in queries. */
  class Entries(_tableTag: Tag) extends profile.api.Table[EntriesRow](_tableTag, "entries") {
    def * = (id, entry, sentiment, journalId) <> (EntriesRow.tupled, EntriesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(entry), Rep.Some(sentiment), journalId)).shaped.<>({r=>import r._; _1.map(_=> EntriesRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column entry SqlType(varchar), Length(100,true) */
    val entry: Rep[String] = column[String]("entry", O.Length(100,varying=true))
    /** Database column sentiment SqlType(int4) */
    val sentiment: Rep[Int] = column[Int]("sentiment")
    /** Database column journal_id SqlType(int4), Default(None) */
    val journalId: Rep[Option[Int]] = column[Option[Int]]("journal_id", O.Default(None))

    /** Foreign key referencing Journal (database name entries_journal_id_fkey) */
    lazy val journalFk = foreignKey("entries_journal_id_fkey", journalId, Journal)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Entries */
  lazy val Entries = new TableQuery(tag => new Entries(tag))

  /** Entity class storing rows of table Journal
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param title Database column title SqlType(varchar), Length(30,true)
   *  @param year Database column year SqlType(varchar), Length(5,true)
   *  @param director Database column director SqlType(varchar), Length(30,true) */
  case class JournalRow(id: Int, title: String, year: String, director: String)
  /** GetResult implicit for fetching JournalRow objects using plain SQL queries */
  implicit def GetResultJournalRow(implicit e0: GR[Int], e1: GR[String]): GR[JournalRow] = GR{
    prs => import prs._
    JournalRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table journal. Objects of this class serve as prototypes for rows in queries. */
  class Journal(_tableTag: Tag) extends profile.api.Table[JournalRow](_tableTag, "journal") {
    def * = (id, title, year, director) <> (JournalRow.tupled, JournalRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(title), Rep.Some(year), Rep.Some(director))).shaped.<>({r=>import r._; _1.map(_=> JournalRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column title SqlType(varchar), Length(30,true) */
    val title: Rep[String] = column[String]("title", O.Length(30,varying=true))
    /** Database column year SqlType(varchar), Length(5,true) */
    val year: Rep[String] = column[String]("year", O.Length(5,varying=true))
    /** Database column director SqlType(varchar), Length(30,true) */
    val director: Rep[String] = column[String]("director", O.Length(30,varying=true))

    /** Uniqueness Index over (director) (database name journal_director_key) */
    val index1 = index("journal_director_key", director, unique=true)
    /** Uniqueness Index over (title) (database name journal_title_key) */
    val index2 = index("journal_title_key", title, unique=true)
  }
  /** Collection-like TableQuery object for table Journal */
  lazy val Journal = new TableQuery(tag => new Journal(tag))
}
