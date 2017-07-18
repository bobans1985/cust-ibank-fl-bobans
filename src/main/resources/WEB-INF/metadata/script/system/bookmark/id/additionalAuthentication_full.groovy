import com.custsystems.custerp.kernel.custerpProperties.CusterpProperties
import com.custsystems.custerp.kernel.tools.LresTools
import com.custsystems.custerp.setup.SetupStorage
import com.custsystems.groovy.zk.ui.Zulbuilder
import com.custsystems.mon.user.UserData
import com.custsystems.res.types.Res
import com.custsystems.security.SecurityInspector
import com.custsystems.security.authentication.UserDeviceTools
import com.custsystems.security.authentication.additional.AdditionalAuthFactory
import com.custsystems.security.authentication.additional.AdditionalAuthTools
import com.custsystems.security.authentication.additional.IAdditionalAuthComponent
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zul.Div
import org.zkoss.zul.Separator

UserData currentUser = UserData.get();
if (!currentUser.isAuthenticatedAdditional() && SecurityInspector.checkAdditionalAuthRequirement(currentUser)) {
	Res locRes = LresTools.getLres(LresTools.MAIN);
	boolean isAnonymous = currentUser.isAnonymous();
	resultComponent = Zulbuilder.inst().div(align: "center") {
		IAdditionalAuthComponent authComp = null;
		if (isAnonymous) {
			label(value: "Вход в систему не осуществлен")
			separator();
		} else {
			window(width: "450px", sclass: "authentication_window", title: locRes.label.additional_authentication) {
				final Div content = new Div();
				content.setParent(self());
				content.setSclass("authentication_window_content")

				label(value: UserDeviceTools.getMessageIfAuthNeed(), sclass: "authentication_message").setParent(content);

				AdditionalAuthFactory authFactory = AdditionalAuthFactory.getInstance();
				String authCompClassName = AdditionalAuthTools.getAuthComponentClassName(currentUser);
				CusterpProperties compProps = AdditionalAuthTools.getAuthComponentProperties(currentUser);

				final Div d = new Div();
				d.setSclass("authentication-button btn_container_div");
				compProps.set("BUTTON_CONTAINER", d);

				authComp = authFactory.createAdditionalAuthComponent(authCompClassName, compProps);
				authComp.setParent(content);
				authComp.refresh();

				d.setParent(self());

				boolean designView = SetupStorage.getBoolean("system/setup", "UL_DESIGN")
				if (designView) {
					button(label: "Получить сертификат", href: "#anonymous=cert_req&action=generate").setParent(d);
					button(label: "Мои сертификаты", href: "#id=my_certificates").setParent(d);
				}
				button(autodisable: "self", label: "Продолжить") {
					event("onClick") {
						authComp.validate();
					}
				}.setParent(d);
				button(href: "/j_spring_security_logout", label: "Выход", sclass: "form_cancel_btn").setParent(d);
			}
		}
		event('onFocus') {
			if (authComp != null) authComp.setFocus();
		}
		Executions.getCurrent().postEvent(new Event('onFocus', self()))
	}
} else {
	currentUser.setAuthenticatedAdditional(true);
	Executions.sendRedirect(null);
	/*resultComponent = Zulbuilder.inst().div(align: "center", sclass: "trusted-device") {
		window(width: "450px", title: "Подтверждение входа") {
			div(sclass: "trusted_device_window_content") {
				label(value: UserDeviceTools.getMessageIfAuthPassed(), pre: true)
			}
			div(sclass: "btn_container_div") {
				button(autodisable: "self", label: "Продолжить") {
					event("onClick") {
						Executions.sendRedirect(null);
					}
				}
				button(href: "/j_spring_security_logout", label: "Выход", sclass: "form_cancel_btn")
			}
		}
	}*/
}

