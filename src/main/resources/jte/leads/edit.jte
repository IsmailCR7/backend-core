@import ru.mentee.power.crm.model.Lead
@import ru.mentee.power.crm.model.LeadStatus

@param Lead lead

<!DOCTYPE html>
<html>
<head>
    <title>Редактирование лида</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <div class="max-w-md mx-auto bg-white rounded-2xl shadow-xl p-8">
        <h1 class="text-2xl font-bold mb-6 text-blue-700">Редактирование лида</h1>

        <form method="post" action="/leads/${String.valueOf(lead.id())}" class="space-y-5">
            <input type="hidden" name="id" value="${String.valueOf(lead.id())}">

            <div>
                <label for="name" class="block text-sm font-medium text-gray-700 mb-2">
                    Name
                </label>
                <input type="text" id="name" name="name" value="${lead.name()}" required
                       class="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition">
            </div>

            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                    Email
                </label>
                <input type="email" id="email" name="email" value="${lead.email()}" required
                       class="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition">
            </div>

            <div>
                <label for="company" class="block text-sm font-medium text-gray-700 mb-2">
                    Company
                </label>
                <input type="text" id="company" name="company" value="${lead.company()}" required
                       class="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition">
            </div>

            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-2">
                    Status
                </label>
                <select name="status" id="status" class="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500">
                    @if(lead.status().name() == "NEW")
                        <option value="NEW" selected>Новый</option>
                    @else
                        <option value="NEW">Новый</option>
                    @endif

                    @if(lead.status().name() == "CONTACTED")
                        <option value="CONTACTED" selected>Связались</option>
                    @else
                        <option value="CONTACTED">Связались</option>
                    @endif

                    @if(lead.status().name() == "QUALIFIED")
                        <option value="QUALIFIED" selected>Квалифицированный</option>
                    @else
                        <option value="QUALIFIED">Квалифицированный</option>
                    @endif
                </select>
            </div>

            <button type="submit" class="w-full bg-blue-600 text-white px-4 py-2 rounded-xl hover:bg-blue-700 transition duration-200 font-medium">
                Сохранить изменения
            </button>

            <a href="/leads" class="block text-center text-sm text-gray-500 hover:text-gray-700 transition mt-3">
                Отмена
            </a>
        </form>
    </div>
</div>
</body>
</html>