package ru.msk.java.lagompocstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import ru.msk.java.lagompocstream.api.LagompocStreamService
import ru.msk.java.lagompoc.api.LagompocService
import com.softwaremill.macwire._

class LagompocStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagompocStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagompocStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagompocStreamService])
}

abstract class LagompocStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[LagompocStreamService](wire[LagompocStreamServiceImpl])

  // Bind the LagompocService client
  lazy val lagompocService: LagompocService = serviceClient.implement[LagompocService]
}
