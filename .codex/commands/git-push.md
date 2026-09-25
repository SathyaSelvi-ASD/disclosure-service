# Git Change, Commit and Push

<!-- Check my git changes, run the tests, 
create a new commit with an appropriate commit message, and push it to the current branch. -->

Review the current Git changes and, if the changes are valid and related to the requested work:

1. Check `git status`.
2. Check the current branch.
3. Inspect `git diff` and staged changes.
4. Review for bugs, secrets, debug code, and unrelated files.
5. Run the appropriate project tests/validation.
6. Stage only the intended files.
7. Show the files that will be committed and the proposed commit message.
8. Create a new commit.
9. Verify the commit.
10. Push the current branch to `origin`.

Do not force push.

Do not reset or delete changes.

Do not commit secrets or unrelated files.

If validation fails because of the current changes, stop and report the failure.

If unrelated changes are present, exclude them from the commit.

If pushing fails because the remote branch has changed, stop and ask whether to rebase/pull before continuing.

At the end, report the branch, changed files, validation result, commit hash, commit message, and push result.
