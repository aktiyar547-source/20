package androidx.compose.ui.platform
import androidx.compose.runtime.ProvidableCompositionLocal
val LocalContext = object : ProvidableCompositionLocal<android.content.Context>() {}
val LocalConfiguration = object : ProvidableCompositionLocal<Any>() {}
val LocalDensity = object : ProvidableCompositionLocal<Any>() {}

class HapticFeedback { fun performHapticFeedback(type: Any?) {} }
val LocalHapticFeedback = object : androidx.compose.runtime.ProvidableCompositionLocal<HapticFeedback>() {}
