/**
 * Copyright 2014 Adrian Hurtado (adrianhurt)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package views.html.b3

import views.html.b3
import org.specs2.mutable.Specification
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.Lang
import play.twirl.api.Html
import play.api.mvc.Call

object FormsSpec extends Specification {

  val vfc = b3.vertical.fieldConstructor
  val (colLabel, colInput) = ("col-md-2", "col-md-10")
  val hfc = b3.horizontal.fieldConstructor(colLabel, colInput)
  val ifc = b3.inline.fieldConstructor
  val cfc = b3.clear.fieldConstructor
  val lang = implicitly[Lang]

  val testContentString = "<content>"

  val (method, action) = ("POST", "/handleRequest")
  val fooCall = Call(method, action)
  def fooFormBody(args: (Symbol, Any)*)(fc: b3.B3FieldConstructor) = b3.form(fooCall, args: _*)(Html(testContentString))(fc).body

  "@form" should {

    val simple = fooFormBody()(vfc)

    "have action and method" in {
      simple must contain("action=\"" + action + "\"")
      simple must contain("method=\"" + method + "\"")
    }

    "add form role as default" in {
      simple must contain("role=\"form\"")
    }

    "allow setting custom class" in {
      fooFormBody('class -> "customClass")(vfc) must contain("class=\"customClass\"")
    }

    "add default class for each field constructor" in {
      fooFormBody()(vfc) must be equalTo fooFormBody('class -> "form-vertical")(vfc)
      fooFormBody()(hfc) must be equalTo fooFormBody('class -> "form-horizontal")(vfc)
      fooFormBody()(ifc) must be equalTo fooFormBody('class -> "form-inline")(vfc)
      fooFormBody()(cfc) must be equalTo fooFormBody('class -> "form-clear")(vfc)
    }

    "allow setting extra arguments and remove those arguments with false values or with slashed names" in {
      val body = fooFormBody('extra_attr -> "test", 'true_attr -> true, 'fase_attr -> false, '_slashed_attr -> "test")(vfc)
      body must contain("extra_attr=\"test\"")
      body must contain("true_attr=\"true\"")
      body must not contain ("false_attr=\"false\"")
      body must not contain ("_slashed_attr=\"test\"")
    }

    "render the content body" in {
      simple must contain("<content>")
    }
  }
}