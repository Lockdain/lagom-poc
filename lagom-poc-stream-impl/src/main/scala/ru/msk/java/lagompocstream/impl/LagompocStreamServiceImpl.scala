package ru.msk.java.lagompocstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import ru.msk.java.lagompocstream.api.LagompocStreamService
import ru.msk.java.lagompoc.api.LagompocService

import scala.concurrent.Future

/**
  * Implementation of the LagompocStreamService.
  */
class LagompocStreamServiceImpl(lagompocService: LagompocService) extends LagompocStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagompocService.hello(_).invoke()))
  }
}
