package kotlinx.serialization

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Serializable

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class SerialName(val value: String)

interface KSerializer<T>
