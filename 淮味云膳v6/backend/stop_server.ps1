# Stop process on port 8082

Write-Host "Searching for processes on port 8082..."

# Fallback to netstat directly
Write-Host "Using netstat to find processes..."
$netstatOutput = netstat -ano | findstr :8082

if ($netstatOutput) {
    Write-Host "Found processes using port 8082:"
    $netstatOutput | ForEach-Object {
        $parts = $_.Trim() -split '\s+'
        $pid = $parts[-1]
        Write-Host "PID: $pid"
        
        # Stop process
        try {
            Stop-Process -Id $pid -Force
            Write-Host "Successfully stopped PID $pid"
        } catch {
            Write-Host "Failed to stop PID $pid"
        }
    }
    
    # Verify port is released
    Write-Host "`nVerifying port 8082 is released..."
    Start-Sleep -Seconds 2
    $netstatOutput2 = netstat -ano | findstr :8082
    if ($netstatOutput2) {
        Write-Host "Warning: Port 8082 is still in use"
    } else {
        Write-Host "Success: Port 8082 is released"
    }
} else {
    Write-Host "No processes found on port 8082"
}

Write-Host "`nOperation completed. Press any key to exit..."
try {
    $host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown') | Out-Null
} catch {
    # Ignore error if ReadKey fails
    Write-Host "Press Enter to exit..."
    Read-Host
}