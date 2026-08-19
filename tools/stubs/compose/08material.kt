package androidx.compose.material3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
class ColorScheme {
    val primary = Color(); val onPrimary = Color(); val secondary = Color()
    val background = Color(); val onBackground = Color(); val surface = Color()
    val onSurface = Color(); val surfaceVariant = Color(); val onSurfaceVariant = Color()
    val error = Color(); val outline = Color(); val onSecondary = Color()
    val primaryContainer = Color(); val onPrimaryContainer = Color()
}
typealias TextStyle = androidx.compose.ui.text.TextStyle
class Typography(
    val displayLarge: TextStyle = TextStyle(),
    val displayMedium: TextStyle = TextStyle(),
    val displaySmall: TextStyle = TextStyle(),
    val headlineLarge: TextStyle = TextStyle(),
    val headlineMedium: TextStyle = TextStyle(),
    val headlineSmall: TextStyle = TextStyle(),
    val titleLarge: TextStyle = TextStyle(),
    val titleMedium: TextStyle = TextStyle(),
    val titleSmall: TextStyle = TextStyle(),
    val bodyLarge: TextStyle = TextStyle(),
    val bodyMedium: TextStyle = TextStyle(),
    val bodySmall: TextStyle = TextStyle(),
    val labelLarge: TextStyle = TextStyle(),
    val labelMedium: TextStyle = TextStyle(),
    val labelSmall: TextStyle = TextStyle(),
)
object MaterialTheme {
    val colorScheme: ColorScheme @Composable get() = ColorScheme()
    val typography: Typography @Composable get() = Typography()
}
@Composable fun MaterialTheme(colorScheme: ColorScheme? = null, typography: Typography? = null,
    content: @Composable () -> Unit) {}
fun lightColorScheme(
    primary: Color = Color(), onPrimary: Color = Color(),
    primaryContainer: Color = Color(), onPrimaryContainer: Color = Color(),
    secondary: Color = Color(), onSecondary: Color = Color(),
    secondaryContainer: Color = Color(), onSecondaryContainer: Color = Color(),
    tertiary: Color = Color(), onTertiary: Color = Color(),
    background: Color = Color(), onBackground: Color = Color(),
    surface: Color = Color(), onSurface: Color = Color(),
    surfaceVariant: Color = Color(), onSurfaceVariant: Color = Color(),
    error: Color = Color(), onError: Color = Color(),
    errorContainer: Color = Color(), onErrorContainer: Color = Color(),
    outline: Color = Color(), outlineVariant: Color = Color(),
    scrim: Color = Color(), inverseSurface: Color = Color(),
): ColorScheme = ColorScheme()
fun darkColorScheme(
    primary: Color = Color(), onPrimary: Color = Color(),
    primaryContainer: Color = Color(), onPrimaryContainer: Color = Color(),
    secondary: Color = Color(), onSecondary: Color = Color(),
    secondaryContainer: Color = Color(), onSecondaryContainer: Color = Color(),
    tertiary: Color = Color(), onTertiary: Color = Color(),
    background: Color = Color(), onBackground: Color = Color(),
    surface: Color = Color(), onSurface: Color = Color(),
    surfaceVariant: Color = Color(), onSurfaceVariant: Color = Color(),
    error: Color = Color(), onError: Color = Color(),
    errorContainer: Color = Color(), onErrorContainer: Color = Color(),
    outline: Color = Color(), outlineVariant: Color = Color(),
    scrim: Color = Color(), inverseSurface: Color = Color(),
): ColorScheme = ColorScheme()
@Composable fun Text(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified,
    fontSize: TextUnit? = null, fontWeight: Any? = null, fontFamily: Any? = null,
    letterSpacing: TextUnit? = null, textAlign: Any? = null, maxLines: Int = Int.MAX_VALUE,
    overflow: Any? = null, style: TextStyle? = null) {}
@Composable fun Button(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true,
    shape: Shape? = null, colors: Any? = null, contentPadding: Any? = null,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {}
@Composable fun OutlinedButton(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true,
    shape: Shape? = null, colors: Any? = null,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {}
@Composable fun TextButton(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {}
@Composable fun Card(modifier: Modifier = Modifier, shape: Shape? = null, colors: Any? = null,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {}
@Composable fun Checkbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier, enabled: Boolean = true) {}
@Composable fun CircularProgressIndicator(modifier: Modifier = Modifier, color: Color = Color.Unspecified,
    strokeWidth: Dp = Dp(4f)) {}
@Composable fun OutlinedTextField(value: String, onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier, enabled: Boolean = true, label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null, isError: Boolean = false, singleLine: Boolean = false,
    shape: Shape? = null, colors: Any? = null, visualTransformation: Any? = null,
    keyboardOptions: Any? = null, trailingIcon: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null) {}
object OutlinedTextFieldDefaults { @Composable fun colors(focusedBorderColor: Color = Color.Unspecified,
    unfocusedBorderColor: Color = Color.Unspecified): Any = Any() }
object ButtonDefaults { @Composable fun buttonColors(containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified): Any = Any() }
object CardDefaults { @Composable fun cardColors(containerColor: Color = Color.Unspecified): Any = Any() }
@Composable fun AlertDialog(onDismissRequest: () -> Unit, confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier, dismissButton: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null, text: (@Composable () -> Unit)? = null) {}
@Composable fun Scaffold(modifier: Modifier = Modifier, topBar: @Composable () -> Unit = {},
    containerColor: Color = Color.Unspecified,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit) {}
annotation class ExperimentalMaterial3Api
@Composable fun TopAppBar(title: @Composable () -> Unit, modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {}, colors: Any? = null) {}
object TopAppBarDefaults { @Composable fun topAppBarColors(containerColor: Color = Color.Unspecified,
    titleContentColor: Color = Color.Unspecified,
    navigationIconContentColor: Color = Color.Unspecified): Any = Any() }
@Composable fun HorizontalDivider(modifier: Modifier = Modifier, thickness: Dp = Dp(1f),
    color: Color = Color.Unspecified) {}
@Composable fun Icon(imageVector: Any?, contentDescription: String?, modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified) {}
@Composable fun IconButton(onClick: () -> Unit, modifier: Modifier = Modifier,
    content: @Composable () -> Unit) {}
@Composable fun DropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {}
@Composable fun DropdownMenuItem(text: @Composable () -> Unit, onClick: () -> Unit) {}
@Composable fun Surface(modifier: Modifier = Modifier, color: Color = Color.Unspecified,
    content: @Composable () -> Unit) {}
@Composable fun LinearProgressIndicator(modifier: Modifier = Modifier,
    progress: (() -> Float)? = null, color: Color = Color.Unspecified) {}
