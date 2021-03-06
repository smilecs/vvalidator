/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package com.afollestad.vvalidator.field.input

import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.ContainsAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.EmailAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.LengthAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.NumberAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.RegexAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.UriAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.UrlAssertion
import com.afollestad.vvalidator.assertion.text
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.util.resName
import com.google.android.material.textfield.TextInputLayout

/**
 * Represents an edit text field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class InputLayoutField internal constructor(
  container: ValidationContainer,
  @IdRes override val id: Int,
  override val name: String
) : FormField<InputLayoutField, TextInputLayout>() {

  init {
    onErrors { view, errors ->
      view.error = errors.firstOrNull()
          ?.toString()
    }
  }

  override val view = container.getViewOrThrow<TextInputLayout>(id)
  val editText = view.editText ?: throw IllegalStateException(
      "TextInputLayout ${id.resName(container.context)} should have a child EditText."
  )

  /** Asserts that the input text is not empty. */
  fun isNotEmpty() = assert(NotEmptyAssertion())

  /**
   * A wrapper around [conditional] which applies inner assertions only if the
   * input text is not empty.
   */
  fun isEmptyOr(builder: InputLayoutField.() -> Unit) {
    conditional(
        condition = {
          view.text()
              .trim()
              .isNotEmpty()
        },
        builder = builder
    )
  }

  /** Asserts that the input text is a valid URL. */
  fun isUrl() = assert(UrlAssertion())

  /** Asserts that the input text is a valid URI. */
  fun isUri() = assert(UriAssertion())

  /** Asserts that the input text is a valid email address. */
  fun isEmail() = assert(EmailAssertion())

  /** Asserts that the input text is a valid number. */
  fun isNumber() = assert(NumberAssertion())

  /** Asserts that the input text contains a string. */
  fun length() = assert(LengthAssertion())

  /** Asserts that the input text contains a string. */
  fun contains(text: String) = assert(ContainsAssertion(text))

  /** Asserts that the input text matches a regular expression. */
  fun matches(regex: String) = assert(RegexAssertion(regex))

  /** Adds a custom inline assertion for the input layout field. */
  fun assert(
    description: String,
    matcher: (TextInputLayout) -> Boolean
  ) = assert(CustomViewAssertion(description, matcher))
}
