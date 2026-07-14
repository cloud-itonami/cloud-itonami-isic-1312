# cloud-itonami-isic-1312 — Textile Weaving Plant Operations Actor

Community Textile Weaving (ISIC 1312) actor for cloud-itonami — an autonomous plant operations coordination system for fabric production plants.

## Architecture

This actor applies the itonami/langgraph-clj actor pattern: proposals → independent Governor → escalation → human review.

### Governor Rules (Compliance Layer)

**HARD violations** (cannot be overridden):
1. **Spec-basis** — All proposals must cite official regulatory standards
2. **Plant/Batch verification** — Operations require pre-verified plants and production batches
3. **Quality escalation** — Fabric quality defects always escalate to human review
4. **Loom-control block** — Proposals mentioning direct loom equipment operation (warp tension, shuttle speed, weave pattern) are immediately rejected (those remain engineer exclusive authority)

**SOFT violations** (require human sign-off):
5. **Confidence floor** — Proposals below 0.6 confidence escalate to human review
6. **High-stakes actuation** — Batch logging and quality flagging always require human approval

### Operations

- **`:log-weaving-batch`** — Routine fabric batch completion logging
- **`:schedule-loom-maintenance`** — Equipment maintenance scheduling proposal
- **`:flag-fabric-quality`** — Surface a fabric-quality/weave-defect issue (ALWAYS escalates)
- **`:coordinate-fabric-shipment`** — Outbound product shipment coordination

### Scope Boundary (What This Actor Does NOT Do)

This actor **coordinates logistics and compliance paperwork** around textile production. It explicitly does **NOT**:
- Control loom operation or warp setup
- Control shuttle speed, warp tension, or other loom process parameters
- Operate loom equipment directly
- Make process-engineering decisions about weave patterns or material blending

Those decisions remain the exclusive authority of licensed mill engineers.

## Testing

```bash
# Run all tests
clojure -M:test

# Run linting
clojure -M:lint

# Run demo simulation
clojure -M:dev:run
```

## Deployment

Register this actor with cloud-itonami Governor registry:

```bash
gh api /repos/cloud-itonami/cloud-itonami-registry/contents/actors/1312.edn
```

## References

- **ISIC Rev. 5** — International Standard Industrial Classification: 1312 Weaving of textiles
- **Regulatory Standards** (Japan): 労働基準法 §35, 労働安全衛生法 §20, 品質管理基準 §8
- **itonami Pattern** — [ADR-2607062330](https://github.com/kotoba-lang/industry/blob/main/90-docs/adr/2607062330-kototama-actor-host-abi-contract.md)

## License

AGPL-3.0-or-later. See LICENSE file.
