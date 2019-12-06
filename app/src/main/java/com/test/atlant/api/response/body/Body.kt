package com.test.atlant.api.response.body

class LoginBody(val email:String, val password:String)

class LogoutBody(val session_id:String?)