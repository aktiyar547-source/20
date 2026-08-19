package androidx.work
class Configuration {
    class Builder {
        fun setWorkerFactory(f: Any?): Builder = this
        fun setMinimumLoggingLevel(l: Int): Builder = this
        fun setExecutor(e: java.util.concurrent.Executor): Builder = this
        fun build(): Configuration = Configuration()
    }
    interface Provider { val workManagerConfiguration: Configuration }
}
