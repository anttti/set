# #{} game in ClojureScript

Learning ClojureScript, Reagent and Re-frame by implementing a simple version of the card game #{} (Set. Ha-ha. Please do not sue me.).

## Development mode
To start the Figwheel compiler, navigate to the project folder and run the following command in the terminal:

```
npm run dev
```

It will build the CSS, start Figwheel and open the app in your browser.

## REPL

The project is setup to start nREPL on port `7002` once Figwheel starts.
Once you connect to the nREPL, run `(cljs)` to switch to the ClojureScript REPL.

### Building for production

```
npm run build
```
