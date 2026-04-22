# 批量重命名图片文件并更新数据库
# 将 dish_时间戳_中文名.png 重命名为 dish_时间戳.png

$uploadDir = "d:\AAA git\淮味云膳v6 - 改进版 - 副本\淮味云膳v6\backend\uploads\dishes"

Get-ChildItem -Path $uploadDir -File | ForEach-Object {
    $file = $_
    $oldName = $file.Name

    # 匹配格式: dish_数字_中文.png 或 dish_数字_英文.png
    if ($oldName -match '^dish_(\d+)_.+(\.[^.]+)$') {
        $timestamp = $matches[1]
        $extension = $matches[2]
        $newName = "dish_$timestamp$extension"

        if ($oldName -ne $newName) {
            $oldPath = Join-Path $uploadDir $oldName
            $newPath = Join-Path $uploadDir $newName

            # 重命名文件
            Rename-Item -Path $oldPath -NewName $newName -Force
            Write-Host "重命名: $oldName -> $newName"

            # 更新数据库中的路径
            $dbPathOld = "/uploads/dishes/" + $oldName
            $dbPathNew = "/uploads/dishes/" + $newName

            mysql -u root -p123456 -D "huaiwei-v2" -e "UPDATE dish SET image_url = '$dbPathNew' WHERE image_url = '$dbPathOld';"
        }
    }
}

Write-Host "完成!"
