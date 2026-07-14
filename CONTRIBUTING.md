# Contributing to cloud-itonami-isic-1312

## Getting Started

1. Clone this repository
2. Install Clojure/ClojureScript tools
3. Run `clojure -M:test` to verify the test suite passes
4. Create a feature branch for your work

## Code Standards

- All code must be portable Clojure (`.cljc` files where possible, not JVM-only)
- Tests are required for new features
- Use `clojure -M:lint` to check for style issues
- Keep the Governor contract stable — protocol changes require ADR

## Governor Contract Stability

The Governor evaluation protocol is a public contract:

```clojure
(governor/evaluate proposal store)
-> {:holds? boolean
    :hard-violations [...]
    :soft-violations [...]
    :clean? boolean}
```

Breaking changes to this signature or the hard-violation rules require:
1. An ADR documenting the change
2. Coordination with cloud-itonami registry
3. Clear migration path for downstream consumers

## Testing

Add tests in `test/weaving/` for all new functionality:

```bash
# Run all tests
clojure -M:test

# Run with output
clojure -M:test 2>&1 | tee test-output.log
```

## Pull Request Process

1. Ensure all tests pass: `clojure -M:test`
2. Ensure linting passes: `clojure -M:lint`
3. Document changes in commit message referencing ISIC 1312 context
4. Link to any related cloud-itonami ADRs or issues

## Reporting Issues

Security issues: contact maintainers privately

All other issues: use GitHub Issues with a clear title and description of:
- What you were trying to do
- What happened (expected vs. actual)
- Minimal reproduction if possible
- Environment (Clojure version, platform)
