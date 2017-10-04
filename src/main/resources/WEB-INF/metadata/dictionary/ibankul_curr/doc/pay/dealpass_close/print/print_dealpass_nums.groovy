import com.custsystems.custerp.dictionary.dictionaryStorage.IDataItemModel
import com.custsystems.custerp.docflow.documentDictionary.DocumentDictionary
import com.custsystems.custtools.StringTools
import com.custsystems.export.value.SimpleTextData

final DocumentDictionary lines = (DocumentDictionary) RECORD.get("DEALPASS_NUMS");
final StringBuilder res = new StringBuilder();
if (lines != null) {
	if (lines.iterator().hasNext()) {
		String lines2 = RECORD.get("FULLNAME");
		res.append("<tr><td class=\"text_hcentr cellLBR\" style=\"font-size: 12pt;\">");
		res.append(StringTools.get(lines2 + " просит закрыть паспорт сделки <br> "));
		res.append("</td></tr>");
		for (IDataItemModel line : lines) {
			res.append("<tr>");
			res.append("<td class=\"text_hcentr cellLBR\" style=\"font-size: 12pt;\">");
			res.append("№ ");
			final String number = StringTools.get(line.getString("DP_NUM"));
			res.append(number.substring(0,8));
			res.append("/")
			res.append(number.substring(8,12));
			res.append("/")
			res.append(number.substring(12,16));
			res.append("/")
			res.append(number.substring(16,17));
			res.append("/")
			res.append(number.substring(17,18));
			res.append(" от " + StringTools.get(line.getString("DP_DATE")));
			res.append("</td>");
		}
		res.append("<tr><td class=\"text_hcentr cellLBR\" style=\"font-size: 12pt;\">");
		res.append("в связи со следующим основанием:");
		res.append("</td></tr>");

		res.append("<tr><td>&#160;</td><td colspan=\"22\" class=\"cellLBR\">&#160;</td></tr>");
	}
}
result = res.length() > 0 ? new SimpleTextData(res.toString()) : null;