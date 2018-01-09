package com.anosi.asset.scheduling;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.anosi.asset.i18n.I18nComponent;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.Materiel;
import com.anosi.asset.service.MaterielService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Component
public class MaterielScheduling {

	@Autowired
	private ConvertRemindMail convertRemindMail;

	/***
	 * 每天0点进行预测性维护检测
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 * @throws ParseException
	 * @throws MalformedTemplateNameException
	 * @throws TemplateNotFoundException
	 * @throws TemplateException
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void checkCalculate() throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException {
		convertRemindMail.checkRemind();
	}

	@Component
	public static class ConvertRemindMail {

		@Autowired
		private MaterielService materielService;
		@Autowired
		private JavaMailSender mailSender;
		@Autowired
		private I18nComponent i18nComponent;

		@Value("${spring.mail.username}")
		private String from;

		@Transactional
		public void checkRemind() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException,
				IOException, MessagingException, TemplateException {
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
			// 设定去哪里读取相应的ftl模板
			cfg.setClassForTemplateLoading(this.getClass(), "/templates/mail/");
			// 在模板文件目录中寻找名称为name的模板文件
			Template template = cfg.getTemplate("remind.ftl");

			for (Materiel ma : materielService.findAll()) {
				// 判断是否预警,如果已经到了需要预警的天数,则发邮件
				if (ma.needRemind()) {
					for (Account account : ma.getDevice().getRemindReceiverList()) {
						MimeMessage mimeMessage = mailSender.createMimeMessage();
						MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
						// 基本设置.
						helper.setFrom(from);// 发送者.
						helper.setTo(account.getEmailAddress());// 接收者.
						helper.setSubject(MessageFormat.format(i18nComponent.getMessage("materiel.remind"),
								ma.getDevice().getSerialNo(), ma.getName()));// 邮件主题.

						// 设置model
						Map<String, Object> model = new HashMap<String, Object>();
						model.put("ma", ma);
						model.put("account", account);

						String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
						helper.setText(html, true);
						// 发送
						mailSender.send(mimeMessage);
					}
				}
			}
		}

	}

}
