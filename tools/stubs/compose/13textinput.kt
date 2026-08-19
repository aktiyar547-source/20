package androidx.compose.ui.text.input
interface VisualTransformation
class PasswordVisualTransformation : VisualTransformation
class KeyboardType { companion object { val Text = KeyboardType(); val Number = KeyboardType() } }
class ImeAction { companion object { val Done = ImeAction() } }
