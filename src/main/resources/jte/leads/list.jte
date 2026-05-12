@import java.util.List

@import ru.mentee.power.crm.model.Lead
@import ru.mentee.power.crm.model.LeadStatus

@param List<Lead> leads
@param LeadStatus status
@param String name
@param String email
@param String company

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-lg p-6 border-t-4 border-pobeda-blue">
        <div class="flex justify-between items-center mb-6">
            <div>
                <h2 class="text-2xl font-bold text-pobeda-blue">Lead List</h2>
                <p class="text-gray-500 text-sm mt-1">Управление лидами</p>
            </div>
            <div>
                <a
                    href="/leads/new"
                    class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition duration-200 font-medium inline-block"
                >
                    + Добавить лида
                </a>
            </div>
        </div>

        <!-- Search form -->
        <div class="mb-6">
            <form action="/leads" method="get" class="bg-pobeda-light p-4 rounded-lg">
                <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Name</label>
                        <input type="text" name="name" value="${name}"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                        <input type="text" name="email" value="${email}"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Company</label>
                        <input type="text" name="company" value="${company}"
                               class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
                        <select name="status" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pobeda-blue">
                            @if(status == null)
                                <option value="" selected></option>
                            @else
                                <option value=""></option>
                            @endif

                            @if(status == LeadStatus.NEW)
                                <option value="NEW" selected>Новый</option>
                            @else
                                <option value="NEW">Новый</option>
                            @endif

                            @if(status == LeadStatus.CONTACTED)
                                <option value="CONTACTED" selected>Связались</option>
                            @else
                                <option value="CONTACTED">Связались</option>
                            @endif

                            @if(status == LeadStatus.QUALIFIED)
                                <option value="QUALIFIED" selected>Квалифицированный</option>
                            @else
                                <option value="QUALIFIED">Квалифицированный</option>
                            @endif
                        </select>
                    </div>
                </div>
                <div class="mt-4 text-right">
                    <button type="submit" class="bg-pink-500 text-white px-6 py-2 rounded-md hover:bg-pink-600 transition duration-200 font-medium">
                        Найти
                    </button>
                </div>
            </form>
        </div>

        <!-- Table -->
        <div class="overflow-x-auto">
            <table class="min-w-full bg-white border border-gray-200 rounded-lg overflow-hidden">
                <thead class="bg-pobeda-blue text-white">
                    <tr>
                        <th class="px-4 py-3 text-left">Name</th>
                        <th class="px-4 py-3 text-left">Email</th>
                        <th class="px-4 py-3 text-left">Company</th>
                        <th class="px-4 py-3 text-left">Status</th>
                        <th class="px-4 py-3 text-left" colspan="2">Действия</th>
                    </tr>
                </thead>
                <tbody>
                @for(var lead : leads)
                    <tr class="border-t border-gray-200 hover:bg-pobeda-light transition">
                        <td class="px-4 py-3 font-medium">${lead.name()}</td>
                        <td class="px-4 py-3">${lead.email()}</td>
                        <td class="px-4 py-3">${lead.company()}</td>
                        <td class="px-4 py-3">
                            @if(lead.status() == LeadStatus.NEW)
                                <span class="px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">Новый</span>
                            @elseif(lead.status() == LeadStatus.CONTACTED)
                                <span class="px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">Связались</span>
                            @else
                                <span class="px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">Квалифицированный</span>
                            @endif
                        </td>
                        <td class="px-4 py-3">
                            <a href="/leads/${String.valueOf(lead.id())}/edit" class="text-pobeda-blue hover:underline font-medium">
                                Редактировать
                            </a>
                        </td>
                        <td class="px-4 py-3">
                            <form action="/leads/${lead.id().toString()}/delete" method="post" class="inline"
                                  onclick="return confirm('Удалить лида c e-mail: ${lead.email()}?')">
                                <button type="submit" class="bg-red-600 text-white px-3 py-1 rounded-md hover:bg-red-700 transition duration-200 text-sm">
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
`)

<style>
    /* Custom colors in Pobeda Airlines style */
    .bg-pobeda-blue {
        background-color: #0066CC;
    }
    .bg-pobeda-light {
        background-color: #E8F2FF;
    }
    .text-pobeda-blue {
        color: #0066CC;
    }
    .border-pobeda-blue {
        border-color: #0066CC;
    }
    .hover\:bg-pobeda-dark:hover {
        background-color: #0052A3;
    }
    .focus\:ring-pobeda-blue:focus {
        --tw-ring-color: #0066CC;
    }
</style>
`)"