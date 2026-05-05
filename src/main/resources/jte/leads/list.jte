@import java.util.List
@import ru.mentee.power.crm.model.Lead
@import ru.mentee.power.crm.model.LeadStatus

@param List<Lead> leads
@param LeadStatus currentFilter
@param String search

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <!-- Заголовок с кнопкой добавления -->
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-2xl font-bold">Список лидов</h2>

            <a href="/leads/new"
               class="bg-green-500 hover:bg-green-600 text-white font-medium px-4 py-2 rounded-lg transition-colors duration-200 flex items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
                </svg>
                Добавить лида
            </a>
        </div>

        <!-- Форма поиска -->
        <div class="bg-white shadow rounded-lg p-4 mb-6">
            <form method="get" action="/leads" class="flex gap-4 items-end">
                <div class="flex-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">
                        Поиск по email или компании
                    </label>
                    <input type="text"
                           name="search"
                           value="@if(search != null)${search}@endif"
                           placeholder="Введите email или компанию..."
                           class="w-full px-3 py-2 border border-gray-300 rounded-md">
                </div>

                <div class="w-48">
                    <label class="block text-sm font-medium text-gray-700 mb-1">
                        Статус
                    </label>
                    <select name="status" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                        <option value="">Все статусы</option>
                        @for(var status : LeadStatus.values())
                            @if(currentFilter != null && currentFilter.toString() == status.toString())
                                <option value="${status.toString()}" selected>${status.toString()}</option>
                            @else
                                <option value="${status.toString()}">${status.toString()}</option>
                            @endif
                        @endfor
                    </select>
                </div>

                <div class="flex gap-2">
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
                        🔍 Найти
                    </button>
                    <a href="/leads" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                        ✖️ Сбросить
                    </a>
                </div>
            </form>
        </div>

        @if(leads.isEmpty())
            <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <p class="text-yellow-600">Нет данных</p>
            </div>
        @else
            <table class="min-w-full bg-white border border-gray-200">
                <thead class="bg-gray-100">
                    <tr>
                        <th class="px-4 py-2 text-left">Email</th>
                        <th class="px-4 py-2 text-left">Company</th>
                        <th class="px-4 py-2 text-left">Status</th>
                        <th class="px-4 py-2 text-left">Created At</th>
                        <th class="px-4 py-2 text-left">Действия</th>
                    </tr>
                </thead>
                <tbody>
                    @for(var lead : leads)
                        <tr class="border-t hover:bg-gray-50">
                            <td class="px-4 py-2">${lead.getEmail()}</td>
                            <td class="px-4 py-2">${lead.getCompany()}</td>
                            <td class="px-4 py-2">
                                @if(lead.getStatus().toString() == "NEW")
                                    <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                        ${lead.getStatus()}
                                    </span>
                                @elseif(lead.getStatus().toString() == "CONTACTED")
                                    <span class="px-2 py-1 rounded text-sm bg-blue-100 text-blue-800">
                                        ${lead.getStatus()}
                                    </span>
                                @elseif(lead.getStatus().toString() == "QUALIFIED")
                                    <span class="px-2 py-1 rounded text-sm bg-purple-100 text-purple-800">
                                        ${lead.getStatus()}
                                    </span>
                                @elseif(lead.getStatus().toString() == "LOST")
                                    <span class="px-2 py-1 rounded text-sm bg-red-100 text-red-800">
                                        ${lead.getStatus()}
                                    </span>
                                @else
                                    <span class="px-2 py-1 rounded text-sm bg-gray-100 text-gray-800">
                                        ${lead.getStatus()}
                                    </span>
                                @endif
                            </td>
                            <td class="px-4 py-2 text-sm text-gray-500">${lead.getCreatedAt().toString()}</td>
                            <td class="px-4 py-2">
                                <a href="/leads/${lead.getId().toString()}/edit"
                                   class="bg-pink-100 hover:bg-pink-200 text-pink-700 px-3 py-1 rounded border border-pink-300">
                                    Редактировать
                                </a>
                                <form method="post" action="/leads/${lead.getId().toString()}/delete"
                                     class="inline"
                                     onsubmit="return confirm('Удалить лида ${lead.getCompany()}?')">
                                  <button type="submit"
                                         class="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700">
                                     Удалить
                                  </button>
                                </form>
                            </td>
                        </tr>
                    @endfor
                </tbody>
            </table>
        @endif
    </div>
`)</ete>