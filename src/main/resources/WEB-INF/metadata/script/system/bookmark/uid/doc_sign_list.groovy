import com.custsystems.cryptocore.widgets.dictionaryWidgets.sign.documentSignListWidget.DocumentSignListWidgetRaund
import com.custsystems.custerp.kernel.custerpProperties.CusterpProperties
import com.custsystems.custerp.kernel.tools.BookmarkTools
import com.custsystems.url.document.UrlData

final UrlData urlData = new UrlData(BookmarkTools.getBookmarkMap());
final DocumentSignListWidgetRaund widget = new DocumentSignListWidgetRaund();
widget.setProperties(new CusterpProperties().set("SELECT_CONTEXT", urlData.getSelectContext()));
resultComponent = widget;