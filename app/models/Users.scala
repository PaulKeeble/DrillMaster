package models

import play.api.Play
import play.api.Logger

object Users {
  val AdminPass = Play.current.configuration.getString("admin.password")
  val AppPass = Play.current.configuration.getString("app.password")
  
  def authenticate(username: String, password: String): Option[String] = {
    Some(password) match {
      case AdminPass => if(username=="admin") Some("admin") else None
      case AppPass => if(username=="tier1ops") Some("tier1ops") else None
      case _ => None
    }
  }
}