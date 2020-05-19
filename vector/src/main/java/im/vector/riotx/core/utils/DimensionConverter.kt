/*
 * Copyright 2019 New Vector Ltd
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
package im.vector.riotx.core.utils

import android.content.res.Resources
import android.util.TypedValue
import javax.inject.Inject

class DimensionConverter @Inject constructor(val resources: Resources) {

    fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                resources.displayMetrics
        ).toInt()
    }

    fun spToPx(sp: Int): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp.toFloat(),
                resources.displayMetrics
        ).toInt()
    }

    fun pdToDp(px: Int): Int {
        return (px.toFloat() / resources.displayMetrics.density).toInt()
    }
}
