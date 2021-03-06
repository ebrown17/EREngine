// logback.groovy config file must be on classpath along with Groovy to be read

import ch.qos.logback.classic.AsyncAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.FileAppender

// logs configuration details
statusListener(OnConsoleStatusListener)

// period to scan for config file changes
scan('1 Minutes')

// config file constants
def logFileDate = timestamp('yyyy-MM-dd_HHmmss')
def defaultLogPattern = "%d{HH:mm:ss.SSS} %-5level [%thread] %logger{36}.%msg%n"


appender("ROLLING", RollingFileAppender) {
    file = "test.log"

    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "erengine-%d.log.gz"
        maxHistory = 14
        totalSizeCap = "5GB"
    }

    encoder(PatternLayoutEncoder) { pattern = defaultLogPattern }

}

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) { pattern = defaultLogPattern }
}

appender("ASYNC",AsyncAppender){
    queueSize = 500
    discardingThreshold = 0
    appenderRef("ROLLING")
}

// Log will display all levels that are greater than set level
// TRACE < DEBUG < INFO < WARN < ERROR < OFF

// Log level for logger in these classes, will override root level if set, will duplicate logs if same appender is added
//logger('io.netty',WARN)

// Root log level for all logging
root(DEBUG, ["STDOUT"])
