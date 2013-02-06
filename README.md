# updoc

This is a little tool with a very specific use case. It's for when you want to
email somebody about the closed issues in a Github issue milestone. Ever wanted
to do that? No? Well, whatever.

## Usage

This is an application. You've got two choices for getting it. The easiest is to
download it from [here](http://raynes.me/updoc/updoc-0.1.0). The not quite as
easy way is to clone this repository and run `lein uberjar` which requires that
you have [leiningen](https://github.com/technomancy/leiningen) installed.

If you downloaded `updoc-0.1.0`, you have a cross platform standalone
executable. All it requires is that you have `java` installed. You can just run
it.

```
penumbra:updoc raynes$ ./updoc-0.1.0 
Usage:

 Switches                     Default  Desc                                       
 --------                     -------  ----                                       
 -p, --no-preview, --preview  false    Create a temp file with the email content. 
 -h, --no-help, --help        true     Show help.                                 
```

You want to call it like so:

```
penumbra:updoc raynes$ updoc-0.1.0 foo@bar.com Raynes pointlesstestrepo foo
Github username: Raynes
Github password: 
```

This will send an email to `foo@bar.com` with all of the closed issues in
milestone `foo` of repo `pointlesstestrepo` under the account/organization
`Raynes`.

If you'd like to get a preview of the email before you send it, run it with the
`-p` argument. This will create a temporary html file of the email that you can
view in your browser. When you want to actually send the email, just run the
command again without the `-p` arg.

`updoc` requires that `sendmail` or some similar equivalent program be available
and configured to send email without extra information.

## Why?

Because we wanted it at [Geni](http://geni.com).

## License

Copyright © 2012 Anthony Grimes

Distributed under the Eclipse Public License, the same as Clojure.
