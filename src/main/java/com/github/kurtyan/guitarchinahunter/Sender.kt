package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import com.github.kurtyan.guitarchinahunter.schedule.RetriableScheduler
import com.guilhermechapiewski.fluentmail.email.EmailMessage
import org.slf4j.LoggerFactory
import java.util.concurrent.Future

/**
 * Created by yanke on 2016/5/8.
 */
class Sender(val smtpServer: String, val smtpUsername: String, val smtpPasword: String, val emailSender: String) {

    val log = LoggerFactory.getLogger(javaClass)
    val executor = RetriableScheduler()

    fun send(entry: ThreadEntry, keyword: String, receiver: String): Future<*> {
        return executor.submit(
                5,
                this.buildCommand(entry, keyword, receiver)
        )
    }

    fun buildCommand(entry: ThreadEntry, keyword: String, receiver: String): Runnable {
        return Runnable {
            try {
                log.info("will begin to send mail");

                EmailMessage().from(emailSender)
                        .to(receiver)
                        .withSubject("thread mathcing keyword: $keyword found")
                        .withBody(entry.toString())
                        .send();

                log.info("send email succeeded");
            } catch (e: Exception) {
                log.error("send email failed", e);
                throw e;
            }
        }
    }

}