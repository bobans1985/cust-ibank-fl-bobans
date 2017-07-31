package com.custsystems.crypto.jacarta.event;

import com.custsystems.custerp.kernel.systemEvents.ContentSecurityPolicyInitEvent;
import com.custsystems.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Event;

/**
 * Created by GrishukovVM on 28.07.2017.
 */
public class JacartaContentSecurityPolicyInitiatorRaund  extends EventListener {
    @Override
    public void onEvent(Event event) throws Exception {
        ContentSecurityPolicyInitEvent initEvent = (ContentSecurityPolicyInitEvent)event;
        initEvent.addPolicy("connect-src", "https://localhost:24738");
    }
}
