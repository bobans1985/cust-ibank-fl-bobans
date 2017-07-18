package com.custsystems.cryptocore.widgets.dictionaryWidgets.sign.documentSignListWidget;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;

/**
 * Created by GrishukovVM on 12.07.2017.
 */
public class DocumentSignListWidgetRaund extends DocumentSignListWidget {
    @Override
    public boolean prepare() {
        super.prepare();
        this.createButtonArea();
        Button btnClose = new Button("Назад");
        btnClose.setClass("form_close_btn");
        //this.getComponensParent().setStyle("margin:3px");
        this.getButtonsParent().setClass("btn_container_div");
        btnClose.setParent(this.getButtonsParent());
        btnClose.addEventListener("onClick", new EventListener() {
            public void onEvent(Event event) throws Exception {
                Clients.evalJavaScript("history.back();");

            }
        });
        return true;
    }
}
