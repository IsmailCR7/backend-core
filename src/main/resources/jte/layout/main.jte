@param gg.jte.Content content

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EagleCRM - Lead Management by Ismail</title>
    <!-- Favicon с орлом -->
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.png">
    <link rel="icon" type="image/x-icon" href="/favicon.png">
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">

    <!-- Навигационная панель -->
    <nav class="bg-white shadow-lg">
        <div class="max-w-7xl mx-auto px-4">
            <div class="flex justify-between h-16">
                <div class="flex items-center">
                    <h1 class="text-xl font-bold text-gray-800">EagleCRM System</h1>
                </div>
                <div class="flex items-center space-x-4">
                    <a href="/leads" class="text-gray-700 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium">
                        Лиды
                    </a>
                </div>
            </div>
        </div>
    </nav>

    <!-- Основной контент -->
    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        ${content}
    </main>

    <!-- Подвал -->
    <footer class="bg-white shadow-lg mt-8">
        <div class="max-w-7xl mx-auto py-4 px-4 text-center text-gray-500 text-sm">
            © 2026 CRM Project. Все права защищены.
        </div>
    </footer>


</body>
</html>