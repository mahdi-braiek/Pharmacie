# Chemins relatifs au dossier du script
$rootPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$jarPath = Join-Path $rootPath "lib\mysql-connector-j-8.0.33.jar"
$binPath = Join-Path $rootPath "bin"
if (-not (Test-Path $binPath)) {
    New-Item -ItemType Directory -Path $binPath | Out-Null
}

$sources = Get-ChildItem (Join-Path $rootPath "src\*.java") | Select-Object -ExpandProperty FullName
javac -cp $jarPath -d $binPath $sources
if ($LASTEXITCODE -ne 0) {
    throw "La compilation Java a echoue."
}

$classpath = "$binPath;$jarPath"

# Lancer l'interface graphique LoginGUI
java -cp $classpath LoginGUI
