# Governance

## Architecture Review Board (ARB)

This actor's Governor contract is reviewed by the cloud-itonami ARB before major changes.

## Proposal Review Cycle

1. **Governor Evaluation** — Automatic, deterministic; cannot reject
2. **Hard Violations** → Hold proposal (automatic escalation to human)
3. **Soft Violations** → Hold proposal (requires human sign-off)
4. **Clean Proposal** → Permitted to execute (subject to deployment authorization)

## Regulatory Jurisdiction

This implementation targets **Japan** regulatory framework:

- 労働基準法 (Labor Standards Act) §35
- 労働安全衛生法 (Industrial Safety and Health Act) §20
- 品質管理基準 (Quality Management Standards) §8

Adaptations for other jurisdictions should be forked with modified `weaving.facts/regulatory-standards`.

## Maintenance Cadence

- Security updates: as-needed
- Feature additions: via ADR in kotoba-lang/industry repo
- Test coverage: maintained at 100% of Governor paths
