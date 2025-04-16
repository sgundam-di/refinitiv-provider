package com.di.publisher;

import com.di.parser.TaqCsvReader;
import com.refinitiv.ema.access.*;
import com.refinitiv.ema.rdm.EmaRdm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.refinitiv.ema.rdm.EmaRdm.MMT_MARKET_BY_ORDER;

@Component
public class AppClient implements OmmProviderClient {

	private final Map<String, Long> ricToHandle = new ConcurrentHashMap<>();
	private OmmProvider provider;

	private final TaqCsvReader taqCsvReader;
	private final AtomicBoolean started = new AtomicBoolean(false);

	@Autowired
	public AppClient(@Lazy TaqCsvReader taqCsvReader) {
		this.taqCsvReader = taqCsvReader;
	}
	public void setProvider(OmmProvider provider) {
		this.provider = provider;
	}

	public Long getHandle(String ric) {
		return ricToHandle.get(ric);
	}

	public void removeHandle(String ric){
		ricToHandle.remove(ric);
	}
	@Override
	public void onReqMsg(ReqMsg reqMsg, OmmProviderEvent event) {
		if (reqMsg.domainType() == EmaRdm.MMT_LOGIN) {

			provider.submit(EmaFactory.createRefreshMsg()
							.domainType(EmaRdm.MMT_LOGIN)
							.name(reqMsg.name())
							.complete(true)
							.solicited(true)
							.state(OmmState.StreamState.OPEN, OmmState.DataState.OK,
									OmmState.StatusCode.NONE, "Login accepted"),
					event.handle());
			if (!started.getAndSet(true)) {
				System.out.println("Consumer requested data, starting TAQ reader...");
				taqCsvReader.startReading();
			}
		} else if ( reqMsg.domainType() == MMT_MARKET_BY_ORDER) {
			String ric = reqMsg.name();

			FieldList fields = EmaFactory.createFieldList();
			fields.add(EmaFactory.createFieldEntry().ascii(55, ric));
			fields.add(EmaFactory.createFieldEntry().realFromDouble(270, 100.00));
			fields.add(EmaFactory.createFieldEntry().uintValue(271, 100));

			provider.submit(EmaFactory.createRefreshMsg()
							.name(ric)
							.serviceName("DIRECT_FEED")
							.domainType(MMT_MARKET_BY_ORDER)
							.solicited(true)
							.complete(true)
							.state(OmmState.StreamState.OPEN, OmmState.DataState.OK,
									OmmState.StatusCode.NONE, "Initial image")
							.payload(fields),
					event.handle());

			ricToHandle.put(ric, event.handle());
		}
	}

	@Override public void onRefreshMsg(RefreshMsg refreshMsg, OmmProviderEvent event) {}
	@Override public void onGenericMsg(GenericMsg genericMsg, OmmProviderEvent event) {}
	@Override public void onPostMsg(PostMsg postMsg, OmmProviderEvent event) {}
	@Override public void onReissue(ReqMsg reqMsg, OmmProviderEvent event) {}


	@Override
	public void onClose(ReqMsg reqMsg, OmmProviderEvent event) {
		ricToHandle.values().removeIf(h -> h.equals(event.handle()));
		System.out.println("🔌 Closed stream with handle: " + event.handle());
	}

	@Override
	public void onStatusMsg(StatusMsg statusMsg, OmmProviderEvent event) {
		if (statusMsg.hasState() && statusMsg.state().streamState() == OmmState.StreamState.CLOSED) {
			ricToHandle.values().removeIf(h -> h.equals(event.handle()));
			System.out.println("Stream CLOSED (via status) — handle removed: " + event.handle());
		}
	}

	@Override public void onAllMsg(Msg msg, OmmProviderEvent event) {}
}