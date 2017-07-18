package com.custsystems.cryptocore.scheduler.jobs;

import com.custsystems.crypto.api.ICryptoProvider;
import com.custsystems.cryptocore.scheduler.jobs.CertificateAutoIssueJob;
import com.custsystems.custerp.dictionary.dictionaryStorage.IDataItemModel;
import com.custsystems.custerp.dictionary.dictionaryStorage.SelectContext;
import com.custsystems.custerp.docflow.documentDictionary.DocumentDictionary;
import com.custsystems.custerp.kernel.Context;
import com.custsystems.custerp.kernel.recordSet.DataRecord;
import com.custsystems.custerp.subject.SubjectManager;
import com.custsystems.custtools.StringTools;
import com.custsystems.custtools.log.Logger;
import com.custsystems.integration.crypto.CryptoResourceManager;
import com.custsystems.mon.user.UserData;
import java.io.IOException;
import java.util.Iterator;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CertificateRequestExportJobRound extends CertificateAutoIssueJob {
    private static final Logger log = Logger.getLogger(CertificateRequestExportJobRound.class);

    public Logger getLogger() {
        return log;
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.getLogger().debug("Export of certificate requests started");
        DocumentDictionary dictionary = this.getDictionary();
        dictionary.refreshModel();
        if(dictionary.isEmpty()) {
            this.getLogger().debug("There are no certificate requests to export");
        } else {
            int counter = 0;
            Iterator i$ = dictionary.iterator();

            while(i$.hasNext()) {
                DataRecord rec = (DataRecord)i$.next();

                try {
                    this.doExport(rec);
                    this.doAfterExport(dictionary, rec);
                    ++counter;
                } catch (IOException var7) {
                    this.getLogger().error((Throwable)var7);
                }
            }

            this.getLogger().debug("Export of certificate requests ended. Number of exported certificate requests: {0}", (Object)Integer.valueOf(counter));
        }
    }

    protected DocumentDictionary getDictionary() {
        DocumentDictionary certificateRequests = (DocumentDictionary)Context.getDictionary("doc/cert_req");
        certificateRequests.setBind("STATUS;FILENAME;FILEDATA;USER_ID;KEY_ID");
        certificateRequests.addFilter("FI_STATUS", "STATUS = {0}", new Object[]{"new"});
        return certificateRequests;
    }

    protected void doExport(IDataItemModel rec) throws IOException {
        Long cryptoId = rec.getLong("CRYPTO_ID");
        ICryptoProvider provider = CryptoResourceManager.getConnector(cryptoId);
        int userId = -1;
        if(rec.getInteger("USER_ID") != null && rec.getInteger("USER_ID").intValue() != 0) {
            userId = rec.getInteger("USER_ID").intValue();
        } else {
            String keyId = rec.getString("GEN_USERNAME");
            if(!StringTools.isEmpty(keyId)) {
                Long csr = SubjectManager.getLastSubjectID(new UserData(keyId));
                if(csr != null) {
                    userId = csr.intValue();
                }
            }
        }

        int keyId1 = rec.getInteger("KEY_ID") == null?0:rec.getInteger("KEY_ID").intValue();
        byte[] csr1 = (byte[])((byte[])rec.get("FILEDATA"));
        String fileName = rec.getString("FILENAME");
        provider.issueCertificate(cryptoId.longValue(), rec.getLong("ID").longValue(), userId, keyId1, csr1, fileName);
    }

    protected void doAfterExport(DocumentDictionary dictionary, IDataItemModel rec) {
        //bobans@ Меняем статус на выгружен/в обработке
        dictionary.executeAction("for_send", new SelectContext(rec));
    }
}
