package eager

import com.google.inject.AbstractModule
import javac.bertModel.BertModel
import javac.javaTokenization.FullTokenizer
import scala.concurrent.Future
import javax.inject._
import play.api.inject.ApplicationLifecycle

//@Singleton
//class OnStartUp {
//  val tokenizer = new FullTokenizer()
//  val bertModel = new BertModel(tokenizer)
//}


// This creates an `ApplicationStart` object once at start-up and registers hook for shut-down.
@Singleton
class ApplicationStart @Inject() (lifecycle: ApplicationLifecycle) {
  // Shut-down hook
  lifecycle.addStopHook { () =>
    Future.successful(())
  }
}

class StartModule extends AbstractModule {
  override def configure() = {
    // bind(classOf[OnStartUp]).asEagerSingleton()
    bind(classOf[ApplicationStart]).asEagerSingleton()
  }
}

