# Git Change, Commit and Push Skill

## Purpose

Review the current Git working tree, identify changed files, validate the changes, create a meaningful Git commit, and push the commit to the appropriate remote branch.

## When to use

Use this skill when the user asks to:

* Check Git changes
* Review modified files
* Create a new commit
* Commit current changes
* Push changes to Git
* Check changes and commit/push them

## Workflow

### 1. Check repository status

Run:

```bash
git status --short
git branch --show-current
git remote -v
```

Identify:

* Current branch
* Modified files
* Added files
* Deleted files
* Untracked files
* Remote repository

Do not modify files at this stage.

### 2. Inspect changes

Run:

```bash
git diff
```

For staged changes:

```bash
git diff --cached
```

If there are many changes, inspect files individually:

```bash
git diff -- <file>
```

Understand what the changes actually do.

### 3. Review the changes

Check for:

* Compilation issues
* Obvious bugs
* Unintended changes
* Debug code
* Secrets/passwords/tokens
* Generated files
* Temporary files
* Incorrect configuration changes
* Test failures
* Formatting problems
* Changes unrelated to the requested work

Never commit credentials, API keys, passwords, private keys, or secrets.

### 4. Check untracked files

Review:

```bash
git status --short
```

Do not automatically add every untracked file.

Exclude files such as:

```text
.idea/
*.iml
target/
build/
node_modules/
.env
*.log
```

unless they are intentionally tracked by the repository.

### 5. Run appropriate validation

For a Maven Spring Boot project, prefer:

```bash
mvn test
```

If tests are not appropriate or would take too long, run at least:

```bash
mvn compile
```

For a Node.js project:

```bash
npm test
```

or the project's configured validation command.

Do not create a commit if validation reveals a failure that is caused by the current changes, unless the user explicitly asks to commit anyway.

### 6. Stage only intended files

Use specific files:

```bash
git add <file1> <file2>
```

Avoid:

```bash
git add .
```

unless it is clear that all current changes belong to the requested work.

Then verify:

```bash
git status
git diff --cached
```

### 7. Create a meaningful commit

Commit messages should describe the actual change.

Examples:

```bash
git commit -m "Add disclosure receipt search API"
```

```bash
git commit -m "Fix disclosure receipt validation"
```

```bash
git commit -m "Add unit tests for disclosure controller"
```

Do not use vague messages such as:

```text
changes
update
fix
testing
new changes
```

### 8. Verify the commit

After committing:

```bash
git status
git log -1 --oneline
```

Confirm that the working tree contains only expected remaining changes.

### 9. Push

Before pushing, verify:

```bash
git branch --show-current
git remote -v
```

Push the current branch:

```bash
git push origin <current-branch>
```

If the branch has no upstream:

```bash
git push -u origin <current-branch>
```

Never force push unless the user explicitly requests it.

Never use:

```bash
git push --force
```

or:

```bash
git push --force-with-lease
```

without explicit user approval.

## Safety Rules

### Never

* Delete user changes without permission
* Run `git reset --hard` without explicit approval
* Run `git clean -fd` without explicit approval
* Force push without explicit approval
* Commit secrets
* Commit unrelated changes
* Rewrite Git history unless explicitly requested
* Automatically resolve merge conflicts by guessing

### If unrelated changes exist

Report them separately.

For example:

```text
I found changes in:
- DisclosureController.java
- DisclosureService.java
- application-local.properties

The first two appear related to the requested change.
application-local.properties contains unrelated local configuration.

I will commit only the first two files.
```

### If the working tree is clean

Report:

```text
Working tree is clean. There are no changes to commit.
```

### If push fails

Do not repeatedly retry blindly.

Report the Git error and explain the likely cause.

Examples:

* Authentication failure
* Permission denied
* Remote branch changed
* Non-fast-forward
* Protected branch
* Merge conflict

For a non-fast-forward error, do not automatically rebase or force push. Ask the user whether they want to pull/rebase and continue.

## Expected Result

At the end, provide:

```text
Git Change Summary
------------------
Branch: <branch>

Changed files:
- <file>

Validation:
- <command>
- Result

Commit:
- <commit hash>
- <commit message>

Push:
- Remote: origin
- Branch: <branch>
- Result
```

## User Confirmation

If the user only asks to "check Git changes", do not automatically commit or push.

If the user explicitly asks to "check, commit and push", perform the complete workflow.

Before a potentially destructive Git operation, request confirmation.
