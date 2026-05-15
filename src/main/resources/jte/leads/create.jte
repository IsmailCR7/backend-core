@import ru.mentee.power.crm.model.Lead

@param Lead lead

<!DOCTYPE html>
<html>
<head>
    <title>Добавить лида - Авиакомпания Победа</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .bg-pobeda-blue {
            background-color: #0056A7;
        }
        .btn-pobeda {
            background-color: #0056A7;
            transition: all 0.3s ease;
        }
        .btn-pobeda:hover {
            background-color: #003d7a;
            transform: translateY(-1px);
        }
    </style>
</head>
<body class="bg-gray-50">

<!-- Шапка -->
<div class="bg-pobeda-blue text-white shadow-lg">
    <div class="container mx-auto p-6">
        <div class="flex items-center">
            <a href="/leads" class="text-white hover:text-blue-200 transition mr-4">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                </svg>
            </a>
            <div>
                <h1 class="text-2xl font-bold">✈️ Добавить нового лида</h1>
                <p class="text-blue-100 text-sm">Заполните информацию о клиенте</p>
            </div>
        </div>
    </div>
</div>

<div class="container mx-auto p-6">
    <div class="max-w-2xl mx-auto">
        <div class="bg-white rounded-lg shadow-md p-8">
            <form action="/leads" method="post" class="space-y-6">
                <div>
                    <label for="name" class="block text-sm font-medium text-gray-700 mb-2">
                        Имя *
                    </label>
                    <input type="text" id="name" name="name" required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-pobeda-blue focus:border-transparent"
                           placeholder="Введите имя клиента">
                </div>

                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                        Email *
                    </label>
                    <input type="email" id="email" name="email" required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-pobeda-blue focus:border-transparent"
                           placeholder="example@company.com">
                </div>

                <div>
                    <label for="company" class="block text-sm font-medium text-gray-700 mb-2">
                        Компания *
                    </label>
                    <input type="text" id="company" name="company" required
                           class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-pobeda-blue focus:border-transparent"
                           placeholder="Название компании">
                </div>

                <div>
                    <label for="status" class="block text-sm font-medium text-gray-700 mb-2">
                        Статус *
                    </label>
                    <select name="status" id="status"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-pobeda-blue">
                        <option value="NEW">🆕 Новый</option>
                        <option value="CONTACTED">📞 Связались</option>
                        <option value="QUALIFIED">✅ Квалифицированный</option>
                    </select>
                </div>

                <div class="flex gap-4 pt-4">
                    <button type="submit" class="flex-1 btn-pobeda text-white px-6 py-3 rounded-lg shadow-md font-semibold">
                        ✈️ Создать лида
                    </button>
                    <a href="/leads" class="flex-1 bg-gray-300 text-gray-700 px-6 py-3 rounded-lg text-center font-semibold hover:bg-gray-400 transition">
                        Отмена
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>