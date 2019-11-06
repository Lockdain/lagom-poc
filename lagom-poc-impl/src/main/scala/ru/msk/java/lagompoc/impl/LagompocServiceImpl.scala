package ru.msk.java.lagompoc.impl

import ru.msk.java.lagompoc.api
import ru.msk.java.lagompoc.api.LagompocService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the LagompocService.
  */
class LagompocServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends LagompocService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagom-poc entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagompocEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagom-poc entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagompocEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(LagompocEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[LagompocEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
