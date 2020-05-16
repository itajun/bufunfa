package au.ivj.bufunfa

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import kotlin.reflect.KClass

@SpringBootApplication
@EnableCaching
@EnableScheduling
@ComponentScan(basePackages = [ "au.ivj.bufunfa" ])
class Program : App(LoginView::class) {

    private lateinit var context: ConfigurableApplicationContext

    override fun init() {
        this.context = SpringApplicationBuilder(Program::class.java).web(WebApplicationType.NONE).run()
        context.autowireCapableBeanFactory.autowireBean(this)
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T = context.getBean(type.java)
            override fun <T : Any> getInstance(type: KClass<T>, name: String): T = context.getBean(type.java, name)
        }
    }

    override fun stop() {
        super.stop()
        context.close()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Program::class.java, *args)
        }
    }
}
