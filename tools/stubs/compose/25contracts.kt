package androidx.activity.result.contract
interface ActivityResultContract<I, O>
object ActivityResultContracts {
    class RequestPermission : ActivityResultContract<String, Boolean>
}
