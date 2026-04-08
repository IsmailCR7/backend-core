@param java.util.List<ru.mentee.power.crm.model.Lead> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter
@param java.lang.String search

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <!-- Заголовок с кнопкой добавления -->
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-2xl font-bold">Список лидов</h2>

            <!-- НОВАЯ ЗЕЛЕНАЯ КНОПКА -->
            <a href="/leads/new"
               class="bg-green-500 hover:bg-green-600 text-white font-medium px-4 py-2 rounded-lg transition-colors duration-200 flex items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
                </svg>
                Добавить лида
            </a>
        </div>

        <!-- ========== ФОРМА ПОИСКА ========== -->
        <div class="bg-white shadow rounded-lg p-4 mb-6">
            <form method="get" action="/leads" class="flex gap-4 items-end">
                <!-- Поле поиска -->
                <div class="flex-1">
                    <label class="block text-sm font-medium text-gray-700 mb-1">
                        Поиск по имени или email
                    </label>
                    <input type="text"
                           name="search"
                           value="@if(search != null)${search}@endif"
                           placeholder="Введите имя или email..."
                           class="w-full px-3 py-2 border border-gray-300 rounded-md">
                </div>

                <!-- Фильтр по статусу -->
                <div class="w-48">
                    <label class="block text-sm font-medium text-gray-700 mb-1">
                        Статус
                    </label>
                    <select name="status" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                        <option value="">Все статусы</option>
                        @if(currentFilter != null && currentFilter.toString() == "NEW")
                            <option value="NEW" selected>Новый</option>
                        @else
                            <option value="NEW">Новый</option>
                        @endif
                        @if(currentFilter != null && currentFilter.toString() == "CONTACTED")
                            <option value="CONTACTED" selected>В контакте</option>
                        @else
                            <option value="CONTACTED">В контакте</option>
                        @endif
                        @if(currentFilter != null && currentFilter.toString() == "QUALIFIED")
                            <option value="QUALIFIED" selected>Квалифицирован</option>
                        @else
                            <option value="QUALIFIED">Квалифицирован</option>
                        @endif
                        @if(currentFilter != null && currentFilter.toString() == "LOST")
                            <option value="LOST" selected>Потерян</option>
                        @else
                            <option value="LOST">Потерян</option>
                        @endif
                    </select>
                </div>

                <!-- Кнопки -->
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
        <!-- ========== КОНЕЦ ФОРМЫ ПОИСКА ========== -->

        <!-- Панель фильтрации -->
        <div class="mb-6">
            <h3 class="text-lg font-semibold mb-2">Фильтр по статусу:</h3>
            <div class="flex flex-wrap gap-2">
                <!-- Кнопка "Все" -->
                @if(currentFilter == null)
                    <a href="/leads" class="px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white">
                        Все
                    </a>
                @else
                    <a href="/leads" class="px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300">
                        Все
                    </a>
                @endif

                <!-- Кнопка "NEW" -->
                @if(currentFilter != null && currentFilter.toString() == "NEW")
                    <a href="/leads?status=NEW" class="px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white">
                        Новые (NEW)
                    </a>
                @else
                    <a href="/leads?status=NEW" class="px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300">
                        Новые (NEW)
                    </a>
                @endif

                <!-- Кнопка "CONTACTED" -->
                @if(currentFilter != null && currentFilter.toString() == "CONTACTED")
                    <a href="/leads?status=CONTACTED" class="px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white">
                        В работе (CONTACTED)
                    </a>
                @else
                    <a href="/leads?status=CONTACTED" class="px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300">
                        В работе (CONTACTED)
                    </a>
                @endif

                <!-- Кнопка "QUALIFIED" -->
                @if(currentFilter != null && currentFilter.toString() == "QUALIFIED")
                    <a href="/leads?status=QUALIFIED" class="px-4 py-2 rounded transition-colors inline-block bg-blue-600 text-white">
                        Квалифицированные (QUALIFIED)
                    </a>
                @else
                    <a href="/leads?status=QUALIFIED" class="px-4 py-2 rounded transition-colors inline-block bg-gray-200 hover:bg-gray-300">
                        Квалифицированные (QUALIFIED)
                    </a>
                @endif
            </div>

            <!-- Информация о текущем фильтре -->
            @if(currentFilter != null)
                <p class="mt-4 text-sm text-gray-600 bg-blue-50 border border-blue-200 rounded-lg p-3">
                    <span class="font-medium">Активный фильтр:</span>
                    показаны лиды со статусом
                    <span class="font-semibold">${currentFilter}</span>
                </p>
            @endif
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
                        <th class="px-4 py-2 text-left">Действия</th>
                    </tr>
                </thead>
                <tbody>
                    @for(var lead : leads)
                        <tr class="border-t hover:bg-gray-50">
                            <td class="px-4 py-2">${lead.email()}</td>
                            <td class="px-4 py-2">${lead.company()}</td>
                            <td class="px-4 py-2">
                                @if(lead.status().toString() == "NEW")
                                    <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                        ${lead.status()}
                                    </span>
                                @elseif(lead.status().toString() == "CONTACTED")
                                    <span class="px-2 py-1 rounded text-sm bg-blue-100 text-blue-800">
                                        ${lead.status()}
                                    </span>
                                @else
                                    <span class="px-2 py-1 rounded text-sm bg-purple-100 text-purple-800">
                                        ${lead.status()}
                                    </span>
                                @endif
                            </td>
                            <td class="px-4 py-2">
                                <a href="/leads/${String.valueOf(lead.id())}/edit"
                                   class="bg-pink-100 hover:bg-pink-200 text-pink-700 px-3 py-1 rounded border border-pink-300">
                                    Редактировать
                                </a>
                                <form method="post" action="/leads/${String.valueOf(lead.id())}/delete"
                                     class="inline"
                                     onsubmit="return confirm('Удалить лида ${lead.company()}?')">
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
`)>