/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.calls.checkers

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.config.isAccessToDescriptorAllowed
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.getSinceVersion

object ApiVersionCallChecker : CallChecker {
    override fun check(resolvedCall: ResolvedCall<*>, reportOn: PsiElement, context: CallCheckerContext) {
        val since = resolvedCall.resultingDescriptor.getSinceVersionIfNotAvailable(context.languageVersionSettings) ?: return
        context.trace.report(Errors.API_NOT_AVAILABLE.on(reportOn, since, context.languageVersionSettings.apiVersion))
    }

    private fun DeclarationDescriptor.getSinceVersionIfNotAvailable(settings: LanguageVersionSettings): String? {
        if (!settings.isAccessToDescriptorAllowed(this)) return getSinceVersion()

        if (this is ConstructorDescriptor) {
            if (!settings.isAccessToDescriptorAllowed(containingDeclaration)) return containingDeclaration.getSinceVersion()
        }

        return null
    }
}
