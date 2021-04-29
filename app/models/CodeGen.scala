package models

object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile",
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/journals?user=jyk&password=password",
    "/Users/user/Desktop/Scala/framework/journal/app/",
    "models", None, None, true, false
  )
}