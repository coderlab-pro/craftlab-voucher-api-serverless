package pro.craftlab.voucher.endpoint.event.consumer.model;

import pro.craftlab.voucher.PojaGenerated;
import pro.craftlab.voucher.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
