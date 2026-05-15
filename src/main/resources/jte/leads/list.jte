@import java.util.List
@import ru.mentee.power.crm.model.Lead
@import ru.mentee.power.crm.model.LeadStatus

@param List<Lead> leads
@param LeadStatus status
@param String name
@param String email
@param String company

<!DOCTYPE html>
<html>
<head>
    <title>Lead List</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <div class="bg-white rounded-2xl shadow-xl p-6">
        <h2 class="text-2xl font-bold mb-4 text-blue-700">Lead List</h2>

        <div class="mb-4">
            <a href="/leads/new" class="bg-green-500 text-white px-3 py-2 rounded-lg hover:bg-green-600 transition duration-200 inline-block">
                + Добавить лида
            </a>
        </div>

        <!-- Search form -->
        <form action="/leads" method="get" class="mb-4 p-4 rounded-xl bg-blue-50">
            <div class="flex flex-wrap gap-3 items-end">
                <label class="text-sm font-medium text-gray-700">
                    Name:
                    <input type="text" name="name" value="${name != null ? name : ""}" class="rounded-lg border-gray-300">
                </label>
                <label class="text-sm font-medium text-gray-700">
                    Email:
                    <input type="text" name="email" value="${email != null ? email : ""}" class="rounded-lg border-gray-300">
                </label>
                <label class="text-sm font-medium text-gray-700">
                    Company:
                    <input type="text" name="company" value="${company != null ? company : ""}" class="rounded-lg border-gray-300">
                </label>
                <label class="text-sm font-medium text-gray-700">
                    Status:
                    <select name="status" class="rounded-lg border-gray-300">
                        <option value="">Все статусы</option>
                        <option value="NEW">Новый</option>
                        <option value="CONTACTED">Связались</option>
                        <option value="QUALIFIED">Квалифицированный</option>
                    </select>
                </label>
                <button type="submit" class="bg-blue-600 text-white rounded-lg px-4 py-1.5 hover:bg-blue-700 transition duration-200">
                    Поиск
                </button>
            </div>
        </form>

        <!-- Table -->
        <div class="overflow-x-auto rounded-xl border border-gray-200">
            <table class="min-w-full bg-white">
                <thead class="bg-blue-700 text-white">
                    <tr>
                        <th class="px-4 py-3 text-left rounded-tl-xl">Name</th>
                        <th class="px-4 py-3 text-left">Email</th>
                        <th class="px-4 py-3 text-left">Company</th>
                        <th class="px-4 py-3 text-left">Status</th>
                        <th class="px-4 py-3 text-left rounded-tr-xl">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    @for(var lead : leads)
                        <tr class="border-t border-gray-100 hover:bg-gray-50 transition duration-150">
                            <td class="px-4 py-3">${lead.name() != null ? lead.name() : ""}</td>
                            <td class="px-4 py-3">${lead.email()}</td>
                            <td class="px-4 py-3">${lead.company()}</td>
                            <td class="px-4 py-3">
                                @if(lead.status().name() == "NEW")
                                    <span class="px-2 py-1 bg-blue-100 text-blue-700 rounded-full text-xs font-medium">Новый</span>
                                @elseif(lead.status().name() == "CONTACTED")
                                    <span class="px-2 py-1 bg-yellow-100 text-yellow-700 rounded-full text-xs font-medium">Связались</span>
                                @else
                                    <span class="px-2 py-1 bg-green-100 text-green-700 rounded-full text-xs font-medium">Квалифицированный</span>
                                @endif
                            </td>
                            <td class="px-4 py-3">
                                <a href="/leads/${String.valueOf(lead.id())}/edit" class="text-blue-600 hover:text-blue-800 font-medium transition">Редактировать</a>
                                <form action="/leads/${String.valueOf(lead.id())}/delete" method="post" class="inline ml-2"
                                      onsubmit="return confirm('Удалить лида ${lead.email()}?')">
                                    <button type="submit" class="bg-red-500 text-white px-3 py-1 rounded-lg hover:bg-red-600 transition duration-200 text-sm">
                                        Удалить
                                    </button>
                                </form>
                            </td>
                        </tr>
                    @endfor
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>