@import ru.mentee.power.crm.model.Lead
@import ru.mentee.power.crm.model.LeadStatus

@param Lead lead
@param LeadStatus[] statuses
@param String error = null

<!DOCTYPE html>
<html>
<head>
    <title>Редактирование лида</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <div class="max-w-md mx-auto bg-white rounded-lg shadow-md p-6">
        <h1 class="text-2xl font-bold mb-6">Редактирование лида</h1>

        @if(error != null)
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                <p>${error}</p>
            </div>
        @endif

        <form method="post" action="/leads/${lead.id()}" class="space-y-4">
            <!-- Поле Email -->
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
                    Email <span class="text-red-500">*</span>
                </label>
                <input type="email"
                       id="email"
                       name="email"
                       value="${lead.email()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <!-- Поле Company -->
            <div>
                <label for="company" class="block text-sm font-medium text-gray-700 mb-1">
                    Компания <span class="text-red-500">*</span>
                </label>
                <input type="text"
                       id="company"
                       name="company"
                       value="${lead.company()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            </div>

            <!-- Поле Status -->
            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">
                    Статус <span class="text-red-500">*</span>
                </label>
                <select id="status" name="status" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="NEW">Новый</option>
                    <option value="CONTACTED">Связались</option>
                    <option value="QUALIFIED">Квалифицированный</option>
                    <option value="LOST">Потерянный</option>
                </select>
            </div>

            <!-- Кнопки -->
            <div class="flex gap-2 pt-4">
                <button type="submit"
                        class="flex-1 bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition">
                    Сохранить
                </button>
                <a href="/leads"
                   class="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-md text-center hover:bg-gray-400 transition">
                    Отмена
                </a>
            </div>
        </form>
    </div>
</div>
</body>
</html>