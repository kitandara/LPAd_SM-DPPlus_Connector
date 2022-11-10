/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.truphone.lpa.impl;

import com.truphone.es9plus.Es9PlusImpl;
import com.truphone.es9plus.message.response.AuthenticateClientRespEs11;
import com.truphone.lpa.ApduChannel;
import com.truphone.lpa.impl.download.ApduTransmitter;
import com.truphone.lpa.impl.download.AuthenticatingPhaseWorker;
import com.truphone.lpa.progress.DownloadProgress;
import com.truphone.util.LogStub;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author amilcar.pereira
 */
public class SmdsRetrieveEventsWorker {

  private static final Logger LOG = Logger.getLogger(SmdsRetrieveEventsWorker.class.getName());

  private final String smdsAddress;
  private final Es9PlusImpl es9Module;
  private final DownloadProgress progress; // SM-DS lookup uses first two phases of a download, so we can borrow...
  private ApduTransmitter apduTransmitter;

  SmdsRetrieveEventsWorker(String smdsAddress, DownloadProgress progress, ApduChannel apduChannel, Es9PlusImpl es9Module) {
    this.smdsAddress = smdsAddress;
    this.apduTransmitter = new ApduTransmitter(apduChannel);
    this.es9Module = es9Module;
    this.progress = progress;
  }

  String[] run() throws Exception {
    AuthenticatingPhaseWorker authenticatingPhaseWorker = new AuthenticatingPhaseWorker(progress, apduTransmitter, es9Module);

    InitialAuthenticationKeys initialAuthenticationKeys = new InitialAuthenticationKeys("",
        smdsAddress,
        authenticatingPhaseWorker.getEuiccInfo(),
        authenticatingPhaseWorker.getEuiccChallenge(""));
    authenticatingPhaseWorker.initiateAuthentication(initialAuthenticationKeys);
    AuthenticateClientRespEs11 es11 = authenticatingPhaseWorker.getAuthenticateClientResponseES11(initialAuthenticationKeys,
        authenticatingPhaseWorker.authenticateWithEuicc(initialAuthenticationKeys));
    LOG.info(LogStub.getInstance().getTag() + " Received " + es11.getEventEntries().length + " events from SM-DS");

    List<String> l = new ArrayList<>();
    // Make activation codes
    for (AuthenticateClientRespEs11.EventEntries e : es11.getEventEntries()) {
      String ac = String.format("1$%s$%s", e.getRspServerAddress(), e.getEventId());
      LOG.info(LogStub.getInstance().getTag() + " Received activation code [" + ac + "]  from SM-DS");
      l.add(ac);
    }
    return l.toArray(new String[0]);
  }
}
