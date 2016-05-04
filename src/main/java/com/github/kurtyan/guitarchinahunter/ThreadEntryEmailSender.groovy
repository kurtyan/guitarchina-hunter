package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import com.github.kurtyan.guitarchinahunter.schedule.RetriableScheduler
import com.guilhermechapiewski.fluentmail.email.EmailMessage
import com.guilhermechapiewski.fluentmail.transport.EmailTransportConfiguration

/**
 * Created by yanke on 2016/5/5.
 */
class ThreadEntryEmailSender {

    def sendEmailExecutor = new RetriableScheduler()

    ThreadEntryEmailSender(smtpServer, smtpUsername, smtpPasword, emailSender) {
        this.smtpServer = smtpServer
        this.smtpUsername = smtpUsername
        this.smtpPasword = smtpPasword
        this.emailSender = emailSender

        EmailTransportConfiguration.configure(smtpServer, true, true, smtpUsername, smtpPasword);
    }

    def send(ThreadEntry entry, String keyword, String receiver) {
        sendEmailExecutor.submit(5) {
            try {
                log.info("will begin to send mail")

                new EmailMessage()
                        .from(emailSender)
                        .to(emailReceiver)
                        .withSubject("thread mathcing keyword: ${keyword} found")
                        .withBody(entry.toString())
                        .send();

                log.info("send email succeeded")
            } catch (Exception e) {
                log.error("send email failed", e)
                throw e
            }
        }


    }


}
