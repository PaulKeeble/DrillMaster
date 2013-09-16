package controllers

import play.api.mvc.RequestHeader

object AuthenticationHelper {
	def isAdmin(request: RequestHeader) : Boolean = request.session.get("user") == Some("admin")
}