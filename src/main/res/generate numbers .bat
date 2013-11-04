REM requires Imagemagick and Roboto font

@echo off
FOR /L %%i IN (1,1,9) DO (
	FOR /L %%j IN (0,1,9) DO (
		echo %%i.%%j
		convert -background transparent -gravity Center -fill #33B5E5 -font "Roboto-Bold-Condensed" -size 48x48 label:"%%i.%%j" drawable-mdpi\v%%i%%j.png
		convert -background transparent -gravity Center -fill #33B5E5 -font "Roboto-Bold-Condensed" -size 72x72 label:"%%i.%%j" drawable-hdpi\v%%i%%j.png
	)
)