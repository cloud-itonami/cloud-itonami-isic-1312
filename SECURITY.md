# Security Policy

## Reporting Vulnerabilities

If you discover a security vulnerability, please email the maintainers **privately** rather than opening a public issue.

Please include:
- Description of the vulnerability
- Steps to reproduce (if applicable)
- Potential impact
- Suggested fix (if you have one)

## Security Considerations

### Governor Integrity

The Governor is the critical security boundary. All proposals must pass Governor evaluation before any action is taken.

**Hard violations cannot be overridden.** Specifically:
- Process-control operations (loom equipment control) are always rejected
- Unverified plants/batches are always rejected
- Quality defects always escalate to human review

### Input Validation

All proposals must:
1. Have a valid `:op` (operation type)
2. Cite regulatory standards (`:cites` non-empty)
3. Include `:effect :propose` (no direct execution)

### Escalation Path

The following are **always** escalated to human review:
- Quality defect flags (`:flag-fabric-quality`)
- Batch logging (`:log-weaving-batch`)
- Loom control mentions (blocks permanently)

## Dependencies

This project uses:
- `langgraph-clj` (langgraph state machine framework)
- `langchain-clj` (LLM integration, not used directly in tests)
- `clj-kondo` (static analysis, dev-only)
- `cognitect/test-runner` (test runner, dev-only)

For dependency security issues, see the upstream projects.

## Compliance

This actor implements controls to comply with:
- Japan: 労働基準法 (Labor Standards Act), 労働安全衛生法 (Industrial Safety and Health Act)
- ISO 9001 (Quality Management)
- ISO 45001 (Occupational Health and Safety)

Deviations from regulatory compliance are security issues and should be reported immediately.
