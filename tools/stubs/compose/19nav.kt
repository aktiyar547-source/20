package androidx.navigation
open class NavController {
    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {}
    fun popBackStack(): Boolean = true
}
class NavHostController : NavController()
class NavOptionsBuilder { fun popUpTo(route: String, b: PopUpToBuilder.() -> Unit = {}) {} }
class PopUpToBuilder { var inclusive: Boolean = false }
class NavBackStackEntry { val arguments: Bundle? = null }
class Bundle { fun getString(k: String): String? = null }
class NavType { companion object { val StringType = NavType(); val LongType = NavType() } }
class NamedNavArgument
fun navArgument(name: String, builder: NavArgumentBuilder.() -> Unit): NamedNavArgument = NamedNavArgument()
class NavArgumentBuilder { var type: NavType = NavType.StringType; var defaultValue: Any? = null }
interface NavGraphBuilder
