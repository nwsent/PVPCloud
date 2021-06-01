package pl.pvpcloud.app.api.module

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ModuleAnnotation(val name: String)